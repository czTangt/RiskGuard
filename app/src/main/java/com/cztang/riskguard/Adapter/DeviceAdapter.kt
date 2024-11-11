package pokercc.android.expandablerecyclerview.sample.markets

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cztang.riskguard.Domain.DeviceDomain
import com.cztang.riskguard.UiUtil.dpToPx
import com.cztang.riskguard.databinding.ViewholderChildDeviceBinding
import com.cztang.riskguard.databinding.ViewholderParentDeviceBinding
import pokercc.android.expandablerecyclerview.ExpandableAdapter

class DeviceChildVH(val binding: ViewholderChildDeviceBinding) :
    ExpandableAdapter.ViewHolder(binding.root)

class DeviceParentVH(val binding: ViewholderParentDeviceBinding) :
    ExpandableAdapter.ViewHolder(binding.root)

class DeviceAdapter(
    private val deviceData: List<DeviceDomain>
) : ExpandableAdapter<ExpandableAdapter.ViewHolder>() {
    override fun onCreateGroupViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ExpandableAdapter.ViewHolder = LayoutInflater.from(viewGroup.context)
        .let { ViewholderParentDeviceBinding.inflate(it, viewGroup, false) }
        .let { DeviceParentVH(it) }

    override fun onCreateChildViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ExpandableAdapter.ViewHolder = LayoutInflater.from(viewGroup.context)
        .let { ViewholderChildDeviceBinding.inflate(it, viewGroup, false) }
        .let { DeviceChildVH(it) }

    override fun onBindChildViewHolder(
        holder: ExpandableAdapter.ViewHolder,
        groupPosition: Int,
        childPosition: Int,
        payloads: List<Any>
    ) {
        holder as DeviceChildVH
        holder.binding.childDevice.text = deviceData[groupPosition].childData

        val childCount = getChildCount(groupPosition)
        val radius = 4.dpToPx()
        val shape = when {
            childCount == 1 -> {
                RoundRectShape(FloatArray(8) { radius }, null, null)
            }

            childPosition == 0 -> {
                RoundRectShape(
                    floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f),
                    null,
                    null
                )
            }

            childPosition == childCount - 1 -> {
                RoundRectShape(
                    floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius),
                    null, null
                )
            }

            else -> {
                RoundRectShape(null, null, null)
            }
        }
        holder.binding.root.background = ShapeDrawable(shape).apply {
            paint.color = Color.WHITE
        }
    }

    override fun onBindGroupViewHolder(
        holder: ExpandableAdapter.ViewHolder,
        groupPosition: Int,
        expand: Boolean,
        payloads: List<Any>
    ) {
        holder as DeviceParentVH
        holder.binding.parentDevice.text = deviceData[groupPosition].parentTitle
        if (payloads.isEmpty()) {
            val arrowImage = holder.binding.arrowDevice
            arrowImage.rotation = if (expand) -180f else 0f
            val circleDrawable = CircleDrawable()
            arrowImage.background = circleDrawable
            circleDrawable.progress = if (expand) 1f else 0f
        }
    }

    override fun onGroupViewHolderExpandChange(
        holder: ExpandableAdapter.ViewHolder,
        groupPosition: Int,
        animDuration: Long,
        expand: Boolean
    ) {
        holder as DeviceParentVH
        val arrowImage = holder.binding.arrowDevice
        arrowImage.animate()
            .setDuration(animDuration)
            .rotation(if (expand) -180f else 0f)
            .setUpdateListener {
                val progress = if (expand) it.animatedFraction else 1 - it.animatedFraction
                (arrowImage.background as CircleDrawable).progress = progress
            }
            .start()
    }

    override fun getGroupCount(): Int = deviceData.size

    override fun getChildCount(groupPosition: Int): Int = 1
}

private class CircleDrawable : ShapeDrawable(OvalShape()) {
    private val argbEvaluator = ArgbEvaluator()
    private val startColor = 0xffEBF1FD.toInt() // light_blue
    private val endColor = 0xff11366A.toInt() // dark_blue
    var progress: Float = 0f
        set(value) {
            paint.color = argbEvaluator.evaluate(value, startColor, endColor) as Int
            invalidateSelf()
        }
}